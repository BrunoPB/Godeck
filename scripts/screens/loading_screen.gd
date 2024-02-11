extends PanelContainer

@onready var user = get_node("/root/User")
@onready var paths = get_node("/root/Constants").loading_screen_paths
@onready var address = get_node("/root/Address")

# Called when the node enters the scene tree for the first time.
func _ready():
	await receive_data()

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(_delta):
	pass

func receive_data():
	get_node(paths["progress_bar"]).value = 0
	var http = HTTPRequest.new()
	add_child(http)
	http.request_completed.connect(start_user)
	http.request(address.BASE_URL+"/test")
	await http.request_completed
	http.queue_free()

func start_user(result, response_code, headers, body):
	if result != 0:
		push_error("Failed getting user data. Code " + str(result))
		return
	var json = JSON.new()
	json.parse(body.get_string_from_utf8())
	var user_data = json.data[0]
	user.id = user_data.id
	user.username = user_data.name
	user.email = user_data.email
	user.gold = user_data.gold
	user.crystals = user_data.crystals
	var collection = []
	for card in user_data.collection:
		collection.append(convert_dictionary_to_card(card))
	user.collection = collection
	var deck = []
	for card in user_data.deck:
		deck.append(convert_dictionary_to_card(card))
	user.deck = deck
	get_node(paths["progress_bar"]).value = 100
	get_tree().change_scene_to_file("res://scenes/main_menu/main_menu.tscn")

func convert_dictionary_to_card(dictionary : Dictionary) -> Card:
	var card : Card = Card.new()
	card.id = dictionary.id
	card.number = dictionary.number
	card.character_name = dictionary.name
	card.tier = dictionary.tier
	card.mythology = dictionary.mythology
	card.file_name = dictionary.fileName
	card.price = dictionary.price
	card.stars = dictionary.stars
	card.north = dictionary.north
	card.north_east = dictionary.northEast
	card.south_east = dictionary.southEast
	card.south = dictionary.south
	card.south_west = dictionary.southWest
	card.north_west = dictionary.northWest
	return card
