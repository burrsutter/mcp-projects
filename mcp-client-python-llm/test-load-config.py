import json

# Path to the JSON file
file_path = "/Users/burr/ai-projects/mcp-projects/mcp-client-python-llm/mcp_servers_config-jdbc.json"

# Load the JSON file
with open(file_path, "r") as file:
    config = json.load(file)

# Print the loaded configuration
print(json.dumps(config, indent=4))