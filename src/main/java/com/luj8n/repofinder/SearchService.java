package com.luj8n.repofinder;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SearchService {
    @NonNull
    public SearchResult search(@NonNull SearchInputs searchInputs) {
        SearchResult result = new SearchResult();

        if (searchInputs.getLink() == null || searchInputs.getLink().isBlank()) {
            if (searchInputs.getOrganization() == null ||
                searchInputs.getOrganization().isBlank()) {
                return result;
            }

            if (searchInputs.getOrganization().contains("/")) {
                if (searchInputs.getOrganization().endsWith("/")) {
                    searchInputs.setOrganization(searchInputs.getOrganization()
                        .substring(0, searchInputs.getOrganization().length() - 1));
                }
                String[] parts = searchInputs.getOrganization().split("/");
                searchInputs.setOrganization(parts[parts.length - 1]);
            }
            searchInputs.setLink(String.format("https://api.github.com/orgs/%s/repos",
                searchInputs.getOrganization().trim()));
        }

        ResponseEntity<JsonNode> response =
            makeRequest(searchInputs, URI.create(searchInputs.getLink()));


        if (response == null || response.getBody() == null) {
            System.err.println("Could not get organization's repos");
            return result;
        }

        String linkHeader = response.getHeaders().getFirst("link");
        if (linkHeader != null) {
            Matcher nextMatcher = Pattern.compile("<([^>]*)>; rel=\"next\"").matcher(linkHeader);
            if (nextMatcher.find()) {
                String nextLink = URLEncoder.encode(nextMatcher.group(1), Charset.defaultCharset());
                result.setNextUrl(
                    String.format("/search?token=%s&link=%s", searchInputs.getToken(), nextLink));
            }

            Matcher prevMatcher = Pattern.compile("<([^>]*)>; rel=\"prev\"").matcher(linkHeader);
            if (prevMatcher.find()) {
                String prevLink = URLEncoder.encode(prevMatcher.group(1), Charset.defaultCharset());
                result.setPrevUrl(
                    String.format("/search?token=%s&link=%s", searchInputs.getToken(), prevLink));
            }
        }

        List<CompletableFuture<RepoResult>> futures = new ArrayList<>();
        for (JsonNode repo : response.getBody()) {
            futures.add(CompletableFuture.supplyAsync(() -> getRepo(repo, searchInputs)));
        }
        CompletableFuture<Void> combined =
            CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));

        combined.thenRun(() -> {
            for (CompletableFuture<RepoResult> future : futures) {
                try {
                    result.getRepos().add(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println(
                        "Couldn't get result: " + e.getMessage());
                }
            }
        });

        combined.join();

        return result;
    }

    private ResponseEntity<JsonNode> makeRequest(@NonNull SearchInputs searchInputs,
                                                 @NonNull URI uri) {
        RequestEntity<Void> request = RequestEntity
            .get(uri)
            .accept(new MediaType("application", "vnd.github+json"))
            .header("Authorization", "Bearer " + searchInputs.getToken())
            .header("X-GitHub-Api-Version", "2022-11-28")
            .build();

        RestTemplate template = new RestTemplate();
        try {
            return template.exchange(request, JsonNode.class);
        } catch (Exception e) {
            // System.err.println("Couldn't complete request: " + e.getMessage());
            return null;
        }
    }

    private RepoResult getRepo(JsonNode repo, SearchInputs searchInputs) {
        return new RepoResult(repo.get("name").asText(), repo.get("html_url").asText(),
            repo.get("description").isNull() ? null : repo.get("description").asText(),
            shouldHighlight(repo.get("contents_url").asText(), searchInputs));
    }

    private boolean shouldHighlight(@NonNull String contentUrl,
                                    @NonNull SearchInputs searchInputs) {
        URI uri = URI.create(contentUrl.replace("{+path}", "README.md"));

        ResponseEntity<JsonNode> response = makeRequest(searchInputs, uri);
        if (response == null || response.getBody() == null) {
            System.err.println("Couldn't get README.md");
            return false;
        }
        String content = response.getBody().get("content").asText();
        if (content == null) {
            return false;
        }
        String decoded = new String(Base64.decodeBase64(content));
        Pattern hasHello = Pattern.compile(".*\\bHello\\b.*", Pattern.MULTILINE);
        return hasHello.matcher(decoded).find();
    }
}
