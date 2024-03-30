extends PanelContainer

@onready var user = get_node("/root/User")

@onready var s_display_name = %DisplayName
@onready var s_elo = %EloPic
@onready var s_gold = %Gold
@onready var s_crystals = %Crystals

# Called when the node enters the scene tree for the first time.
func _ready():
	start_user_data()
	test()

func test():
	var http = HTTPRequest.new()
	add_child(http)
	var token = get_node("/root/Token").TOKEN
	var header = Http_Utils.header(token)
	http.request("http://localhost:8080/friend", header, HTTPClient.METHOD_POST, "user00")

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(_delta):
	pass

func start_user_data():
	s_display_name.text = user.display_name
	s_gold.text = str(user.gold)
	s_crystals.text = str(user.crystals)

func _on_play_button_pressed():
	get_tree().change_scene_to_file("res://scenes/queue_screen.tscn")
