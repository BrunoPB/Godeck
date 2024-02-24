extends PanelContainer

@onready var ingame_system = get_node("/root/InGameSystem")
@onready var queue_system = get_node("/root/QueueSystem")
@onready var paths = get_node("/root/Constants").queue_screen_paths
var delta_time = 0
var queue_time_seconds : int = 0

func _ready():
	queue_system.queue_finished.connect(check_queue)
	queue_system.initiate_queue()

func _process(delta):
	update_time(delta)

func update_time(delta):
	delta_time += delta
	if floor(delta_time) > queue_time_seconds:
		queue_time_seconds = floor(delta_time)
		var seconds = queue_time_seconds%60
		var minutes = queue_time_seconds/60
		var time_string : String = "%01d:%02d" % [minutes, seconds]
		get_node(paths["time_label"]).text = time_string

func check_queue(game_found):
	if(game_found):
		start_game()
	else:
		get_tree().change_scene_to_file("res://scenes/main_menu/main_menu.tscn")

func start_game():
	if ingame_system.establish_connection():
		get_tree().change_scene_to_file("res://scenes/in_game/in_game.tscn")

func _on_cancel_button_pressed():
	queue_system.cancel_queue()
