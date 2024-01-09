extends PanelContainer

@onready var user = get_node("/root/User")
@onready var paths = get_node("/root/Constants").loading_screen_paths
@onready var address = get_node("/root/Address")

# Called when the node enters the scene tree for the first time.
func _ready():
	await receive_data()

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta):
	pass

func receive_data():
	get_node(paths["progress_bar"]).value = 0
	var http = HTTPRequest.new()
	add_child(http)
	http.request_completed.connect(start_user)
	http.request(address.BASE_URL+"/test")
	await http.request_completed
	http.queue_free()
	get_tree().change_scene_to_file("res://scenes/main_menu/main_menu.tscn")

func start_user(result, reponse_code, headers, body):
	var json = JSON.new()
	json.parse(body.get_string_from_utf8())
	var user_data = json.data[0]
	user.id = user_data.id
	user.username = user_data.name
	user.email = user_data.email
	user.gold = user_data.gold
	user.crystals = user_data.crystals
	user.collection = user_data.collection
	user.deck = user_data.deck
	get_node(paths["progress_bar"]).value = 100
