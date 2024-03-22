extends Node

@onready var user = get_node("/root/User")
@onready var address = get_node("/root/Address")
@onready var json_utils = get_node("/root/JsonUtils")

# TODO: Implement completed function, with iOS API and Android API
func login():
	var in_app_store
	var game_center
	if Engine.has_singleton("InAppStore"):
		in_app_store = Engine.get_singleton("InAppStore")
	else:
		print("iOS IAP plugin is not available on this platform.")
	if Engine.has_singleton("GameCenter"):
		game_center = Engine.get_singleton("GameCenter")
	else:
		print("iOS Game Center plugin is not available on this platform.")
	await receive_data()

func receive_data():
	var http = HTTPRequest.new()
	add_child(http)
	http.request_completed.connect(start_user)
	
	###############################################################
	# TODO: DEBUG
	var debugtest = TCPServer.new()
	if debugtest.listen(5000):
		http.request(address.BASE_URL+"/test",PackedStringArray(),HTTPClient.METHOD_GET,"berenice@email.com")
	elif debugtest.listen(5001):
		http.request(address.BASE_URL+"/test",PackedStringArray(),HTTPClient.METHOD_GET,"catarina@email.com")
	else:
		http.request(address.BASE_URL+"/test",PackedStringArray(),HTTPClient.METHOD_GET,"dorival@email.com")
	###############################################################
	
	await http.request_completed
	http.queue_free()

func start_user(result, response_code, headers, body):
	if response_code != 200:
		push_error("Failed getting user data. Code " + str(response_code))
		return
	var user_data = json_utils.get_object_from_string(body.get_string_from_utf8())
	user.start_user(user_data)
