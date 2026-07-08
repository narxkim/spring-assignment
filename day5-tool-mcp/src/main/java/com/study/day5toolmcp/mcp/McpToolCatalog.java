package com.study.day5toolmcp.mcp;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class McpToolCatalog {

    private final ToolCallback[] filesystemTools;
    private final ToolCallback[] fetchTools;
    private final ToolCallback[] allTools;

    public McpToolCatalog(List<McpSyncClient> clients) {
        this.filesystemTools = toolsFrom(named(clients, "filesystem"));
        this.fetchTools = toolsFrom(named(clients, "fetch"));
        this.allTools = toolsFrom(clients);
    }

    public ToolCallback[] filesystemTools() {
        return filesystemTools;
    }

    public ToolCallback[] fetchTools() {
        return fetchTools;
    }

    public ToolCallback[] allTools() {
        return allTools;
    }

    private List<McpSyncClient> named(List<McpSyncClient> clients, String name) {
        return clients.stream()
                .filter(client -> client.getServerInfo().name().contains(name))
                .toList();
    }

    private ToolCallback[] toolsFrom(List<McpSyncClient> clients) {
        return new SyncMcpToolCallbackProvider(clients)
                .getToolCallbacks();
    }

}