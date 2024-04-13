extends PanelContainer

@onready var login_system = get_node("/root/LoginSystem")

@onready var s_progress_bar = %ProgressBar

var login_status = false

func _ready():
	s_progress_bar.value = 0
	await initiate_login_procedure()
	s_progress_bar.value = 100
	if login_status:
		get_tree().change_scene_to_file("res://scenes/main_menu/main_menu.tscn")
	else:
		push_error("Login failed")

func _process(_delta):
	pass

func initiate_login_procedure():
	await login_system.get_token()
	s_progress_bar.value = 30
	login_system.login_status.connect(handle_login)
	login_system.login()
	await login_system.login_status

func handle_login(status : bool):
	login_status = status
