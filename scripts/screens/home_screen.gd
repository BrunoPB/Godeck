extends PanelContainer

@onready var user = get_node("/root/User")

@onready var s_display_name = %DisplayName
@onready var s_elo = %EloPic
@onready var s_gold = %Gold
@onready var s_crystals = %Crystals
@onready var s_platinum = %Platinum

# Called when the node enters the scene tree for the first time.
func _ready():
	start_user_data()

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(_delta):
	pass

func start_user_data():
	s_display_name.text = user.display_name
	s_gold.text = str(user.gold)
	s_crystals.text = str(user.crystals)
	s_platinum.text = str(user.platinum)	

func _on_play_button_pressed():
	get_tree().change_scene_to_file("res://scenes/queue_screen.tscn")
