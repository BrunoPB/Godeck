extends PanelContainer

@onready var ingame_system = get_node("/root/InGameSystem")
@onready var timer = $GameContents/Header/Timer
var game : Game
var delta_time = 0
var time_in_timer : int  = 0
var stop_timer = false

func _ready():
	game = ingame_system.game
	build_ui()
	ingame_system.game_end.connect(end_game)
	ingame_system.check_connection_status()
	ingame_system.listen_to_host()

func _process(delta):
	ingame_system.check_connection_status()
	ingame_system.listen_to_host()
	update_time(delta)

func update_time(delta):
	delta_time += delta
	if floor(delta_time) > time_in_timer and not stop_timer:
		time_in_timer = floor(delta_time)
		if game.time_limit - time_in_timer >= 0:
			var seconds = (game.time_limit - time_in_timer)%60
			var time_string : String = "%02d" % seconds
			timer.text = time_string

func build_ui():
	var seconds = game.time_limit%60
	var time_string : String = "%02d" % seconds
	timer.text = time_string
	$GameContents/Header/EnemyInfo/Enemy.text = game.opponent.opponent_name

func end_game(info : EndGameInfo):
	stop_timer = true
	$PopUpBackground.visible = true
	if info.winner == game.number:
		$PopUpBackground/PopUpLayout/Title.text = "VICTORY!"
	else:
		$PopUpBackground/PopUpLayout/Title.text = "DEFEAT!"
	$PopUpBackground/PopUpLayout/Reason.text = info.reason
	$PopUpBackground/PopUpLayout/Gold.text = "Gold: " + str(info.gold)
	$PopUpBackground/PopUpLayout/Ranking.text = "Ranking: " + str(info.ranking)

func _on_button_pressed():
	ingame_system.send_debug("The test message from client!")

func _on_surrender_pressed():
	ingame_system.declare_surrender()

func _on_endgame_popup_ok_pressed():
	get_tree().change_scene_to_file("res://scenes/main_menu/main_menu.tscn")
