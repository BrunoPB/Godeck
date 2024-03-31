extends Node

@onready var user = get_node("/root/User")
@onready var token = get_node("/root/Token")
@onready var http = HTTPRequest.new()

var test : int = 0
var debugtest = TCPServer.new()

signal login_status(bool)

func _ready():
	###############################################################
	# TODO: DEBUG
	if debugtest.listen(5005) == OK:
		test = 1
	else:
		test = 2
	###############################################################

func get_token():
	var path = Address.TOKEN_PATH if test == 1 else (Address.TOKEN_PATH+"1") # TODO: DEBUG
	var file = FileAccess.open(path, FileAccess.READ)
	if file != null:
		token.TOKEN = file.get_as_text()
	else:
		token.TOKEN = null

func delete_token():
	var path = Address.TOKEN_PATH if test == 1 else (Address.TOKEN_PATH+"1") # TODO: DEBUG
	DirAccess.remove_absolute(path)

func store_new_token(new_token : String):
	var path = Address.TOKEN_PATH if test == 1 else (Address.TOKEN_PATH+"1") # TODO: DEBUG
	var file = FileAccess.open(path, FileAccess.WRITE)
	if file != null:
		token.TOKEN = new_token
		file.store_string(token.TOKEN)

# TODO: Implement completed function
func login():
	add_child(http)
	if check_token():
		await get_user_data()
	elif token.TOKEN != null:
		pass # TODO: Expired token, show login screen
	else:
		await start_ghost_user()
	http.queue_free()

func check_token() -> bool:
	return token.TOKEN != null

func get_user_data():
	http.request_completed.connect(start_user)
	http.request(Address.BASE_URL+"/login",PackedStringArray(),HTTPClient.METHOD_POST,token.TOKEN)
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
