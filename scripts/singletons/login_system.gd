extends Node

@onready var user = get_node("/root/User")
@onready var address = get_node("/root/Address")
@onready var card_utils = get_node("/root/CardUtils")

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
	#http.request(address.BASE_URL+"/test",PackedStringArray(),HTTPClient.METHOD_GET,"catarina@email.com")
	###############################################################
	
	await http.request_completed
	http.queue_free()

func start_user(result, response_code, headers, body):
	if response_code != 200:
		push_error("Failed getting user data. Code " + str(response_code))
		return
	var json = JSON.new()
	json.parse(body.get_string_from_utf8())
	var user_data = json.data
	user.id = user_data.id
	user.username = user_data.name
	user.email = user_data.email
	user.gold = user_data.gold
	user.crystals = user_data.crystals
	var collection = []
	for card in user_data.collection:
		collection.append(card_utils.convert_dictionary_to_card(card))
	user.collection = collection
	var deck = []
	for card in user_data.deck:
		deck.append(card_utils.convert_dictionary_to_card(card))
	user.deck = deck
