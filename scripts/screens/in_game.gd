extends PanelContainer

@onready var ingame_system = get_node("/root/InGameSystem")

func _ready():
	ingame_system.game_end.connect(end_game)
	ingame_system.check_connection_status()
	ingame_system.listen_to_host()

func _process(delta):
	ingame_system.check_connection_status()
	ingame_system.listen_to_host()

func end_game():
	get_tree().change_scene_to_file("res://scenes/main_menu/main_menu.tscn")

func _on_button_pressed():
	ingame_system.send_debug("The test message from client!")

func _on_surrender_pressed():
	ingame_system.declare_surrender()
