extends PanelContainer

@onready var user = get_node("/root/User")

@onready var s_username = %Username
@onready var s_elo = %EloPic
@onready var s_gold = %Gold
@onready var s_crystals = %Crystals

# Called when the node enters the scene tree for the first time.
func _ready():
	start_user_data()

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(_delta):
	pass

func start_user_data():
	s_username.text = user.username
	s_gold.text = str(user.gold)
	s_crystals.text = str(user.crystals)

func _on_play_button_pressed():
	get_tree().change_scene_to_file("res://scenes/queue_screen.tscn")
