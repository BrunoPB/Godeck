extends PanelContainer

@onready var user = get_node("/root/User")
@onready var paths = get_node("/root/Constants").home_screen_paths

# Called when the node enters the scene tree for the first time.
func _ready():
	start_user_data()

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(_delta):
	pass

func start_user_data():
	get_node(paths["username"]).text = user.username
	get_node(paths["gold"]).text = str(user.gold)
	get_node(paths["crystals"]).text = str(user.crystals)

func _on_play_button_pressed():
	get_tree().change_scene_to_file("res://scenes/queue_screen.tscn")
