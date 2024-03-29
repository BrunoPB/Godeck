extends Node

@onready var user = get_node("/root/User")
@onready var token = get_node("/root/Token").token

@onready var http = HTTPRequest.new()

signal login_status(bool)

func get_token():
	var file = FileAccess.open(Address.TOKEN_PATH, FileAccess.READ)
	if file != null:
		token = file.get_as_text()
	else:
		token = null

func delete_token():
	DirAccess.remove_absolute(Address.TOKEN_PATH)

func store_new_token(new_token : String):
	var file = FileAccess.open(Address.TOKEN_PATH, FileAccess.WRITE)
	if file != null:
		token = new_token
		file.store_string(token)

# TODO: Implement completed function
func login():
	add_child(http)
	if check_token():
		await get_user_data()
	elif token != null:
		pass # TODO: Expired token, show login screen
	else:
		await start_ghost_user()
	http.queue_free()

func check_token() -> bool:
	return token != null

func get_user_data():
	http.request_completed.connect(start_user)
	http.request(Address.BASE_URL+"/login",PackedStringArray(),HTTPClient.METHOD_POST,token)
	await http.request_completed

func start_ghost_user():
	http.request_completed.connect(start_user)
	http.request(Address.BASE_URL+"/login/ghostuser",PackedStringArray(),HTTPClient.METHOD_POST)
	await http.request_completed

func start_user(result, response_code, headers, body):
	if response_code != 200:
		push_error("Failed getting user data. Code " + str(response_code))
		return
	var login_response = JSON_Utils.get_object_from_string(body.get_string_from_utf8())
	if login_response.status:
		user.start_user(login_response.user)
		store_new_token(login_response.token)
	else:
		delete_token()
		push_error(login_response.message)
		print(login_response.message)
	login_status.emit(login_response.status)

###############################################################
# TODO: DEBUG
#var debugtest = TCPServer.new()
#if debugtest.listen(5000):
#	http.request(Address.BASE_URL+"/test",PackedStringArray(),HTTPClient.METHOD_GET,"berenice@email.com")
#elif debugtest.listen(5001):
#	http.request(Address.BASE_URL+"/test",PackedStringArray(),HTTPClient.METHOD_GET,"catarina@email.com")
#else:
#	http.request(Address.BASE_URL+"/test",PackedStringArray(),HTTPClient.METHOD_GET,"dorival@email.com")
###############################################################
