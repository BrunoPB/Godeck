extends PanelContainer

@onready var ingame_system = get_node("/root/InGameSystem")
@onready var queue_system = get_node("/root/QueueSystem")
@onready var paths = get_node("/root/Constants").queue_screen_paths
var delta_time = 0
var queue_time_seconds : int = 0
var stop_timer = false
var need_host_confirmation = false

func _ready():
	if not queue_system.queue_finished.is_connected(check_queue):
		queue_system.queue_finished.connect(check_queue)
	queue_system.initiate_queue()
	await queue_system.queue_finished

func _process(delta):
	update_time(delta)
	if need_host_confirmation:
		ingame_system.check_connection_status()
		ingame_system.listen_to_host()

func update_time(delta):
	delta_time += delta
	if floor(delta_time) > queue_time_seconds and not stop_timer:
		queue_time_seconds = floor(delta_time)
		var seconds = queue_time_seconds%60
		var minutes = queue_time_seconds/60
		var time_string : String = "%01d:%02d" % [minutes, seconds]
		get_node(paths["time_label"]).text = time_string

func check_queue(game_found):
	if(game_found):
		prepare_game()
	else:
		get_tree().change_scene_to_file("res://scenes/main_menu/main_menu.tscn")

func prepare_game():
	if ingame_system.establish_connection():
		need_host_confirmation = true
		get_node(paths["cancel_button"]).disabled = true
		stop_timer = true
		ingame_system.game_confirmation.connect(start_game)
		await ingame_system.game_confirmation

func start_game(valid:bool):
	if valid:
		get_tree().change_scene_to_file("res://scenes/in_game/in_game.tscn")
	else:
		need_host_confirmation = false
		queue_system.cancel_queue()

func _on_cancel_button_pressed():
	queue_system.cancel_queue()
