package zinced.client.endpoint

import zinced.client.ZincedClient
import zinced.common.endpoint.ZincedServerInfo

suspend fun ZincedClient.getServerInfo() = callJsonApi<ZincedServerInfo>(url = "/v1/meta/server_info", method = "GET")
