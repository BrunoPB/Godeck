extends PanelContainer

@onready var login_system = get_node("/root/LoginSystem")

@onready var s_progress_bar = %ProgressBarArea/ProgressBar

func _ready():
	s_progress_bar.value = 0
	await login_system.login()
	s_progress_bar.value = 100
	get_tree().change_scene_to_file("res://scenes/main_menu/main_menu.tscn")

func _process(_delta):
	pass
