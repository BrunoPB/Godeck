extends PanelContainer

@onready var user = get_node("/root/User")

# Called when the node enters the scene tree for the first time.
func _ready():
	await receive_data()

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta):
	pass

func receive_data():
	$ProgressBarArea/ProgressBar.value = 0
	$HTTPRequest.request_completed.connect(start_user)
	$HTTPRequest.request("http://localhost:8080/test")
	await $HTTPRequest.request_completed
	get_tree().change_scene_to_file("res://scenes/main_menu.tscn")

func start_user(result, reponse_code, headers, body):
	print(JSON.parse_string(body.get_string_from_utf8()))
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
	$ProgressBarArea/ProgressBar.value = 100
