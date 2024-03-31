extends Node

@onready var in_game_system = get_node("/root/InGameSystem")
@onready var user = get_node("/root/User")
@onready var token = get_node("/root/Token")

var queue_endpoint
var http = HTTPRequest.new()

signal queue_finished(game_found)

func _ready():
	add_child(http)
	queue_endpoint = Address.BASE_URL+"/queue"

func initiate_queue():
	http.cancel_request()
	if not http.request_completed.is_connected(emit_game_found):
		http.request_completed.connect(emit_game_found)
	http.request(queue_endpoint,Http_Utils.header(token.TOKEN),HTTPClient.METHOD_POST,user.id)

func emit_game_found(result, response_code, headers, body):
	if response_code != 200 or result != OK:
		push_error("Failed starting game. Code " + str(response_code))
		return
	var response = JSON_Utils.get_object_from_string(body.get_string_from_utf8())
	if bool(response.status) == false:
		push_error("An error occurred when starting the game. " + str(response.message))
		cancel_queue()
		return
	in_game_system.socket_port = int(response.port)
	queue_finished.emit(true)

func cancel_queue():
	http.request_completed.disconnect(emit_game_found)
	http.cancel_request()
	http.request(queue_endpoint+"/dequeue",Http_Utils.header(token.TOKEN),HTTPClient.METHOD_POST)
	queue_finished.emit(false)
