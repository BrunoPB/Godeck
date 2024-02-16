extends PanelContainer

@onready var paths = get_node("/root/Constants").loading_screen_paths
@onready var login_system = get_node("/root/LoginSystem")

# Called when the node enters the scene tree for the first time.
func _ready():
	get_node(paths["progress_bar"]).value = 0
	await login_system.login()
	get_node(paths["progress_bar"]).value = 100
	get_tree().change_scene_to_file("res://scenes/main_menu/main_menu.tscn")

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(_delta):
	pass
