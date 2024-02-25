extends PanelContainer

@onready var ingame_system = get_node("/root/InGameSystem")

func _ready():
	ingame_system.check_connection_status()
	ingame_system.listen_to_host()

func _process(delta):
	ingame_system.check_connection_status()
	ingame_system.listen_to_host()

func _on_button_pressed():
	ingame_system.send_move()
