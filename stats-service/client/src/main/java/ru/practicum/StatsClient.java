package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {
    private final String appName;

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl,
                       @Value("${app.name}") String appName,
                       RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
        this.appName = appName;
    }

    public ResponseEntity<Object> save(EndpointHitDto endpointHitDto) {
        return post("/hit", endpointHitDto);
    }

    public ResponseEntity<Object> getStats(String start, String end, List<String> uris, Boolean unique) {
        String urisAsString = String.join(",", uris);

        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", urisAsString,
                "unique", unique,
                "app", appName
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}&app={app}", parameters);
    }
}