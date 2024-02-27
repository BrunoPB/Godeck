extends Node

@onready var in_game_system = get_node("/root/InGameSystem")
@onready var user = get_node("/root/User")
@onready var address = get_node("/root/Address")
var http = HTTPRequest.new()

signal queue_finished(game_found)

func _ready():
	add_child(http)
	http.request_completed.connect(emit_game_found)

func initiate_queue():
	http.request(address.BASE_URL+"/queue",PackedStringArray(),HTTPClient.METHOD_POST,user.id)

func emit_game_found(result, response_code, headers, body):
	if response_code != 200 or result != OK:
		push_error("Failed starting game. Code " + str(response_code))
		return
	var json = JSON.new()
	json.parse(body.get_string_from_utf8())
	var response = json.data
	if bool(response.status) == false:
		push_error("An error occurred when starting the game. " + str(response.message))
		cancel_queue()
	in_game_system.socket_port = int(response.port)
	queue_finished.emit(true)

func cancel_queue():
	http.request_completed.disconnect(emit_game_found)
	http.cancel_request()
	http.request(address.BASE_URL+"/queue/dequeue",PackedStringArray(),HTTPClient.METHOD_POST,user.id)
	queue_finished.emit(false)
