extends PanelContainer

@onready var ingame_system = get_node("/root/InGameSystem")
@onready var timer = $GameContents/Header/Timer
var game : Game
var delta_time = 0
var time_in_timer : int  = 0
var stop_timer = false
var current_turn = false
var selected_card_index = -1

func _ready():
	game = ingame_system.game
	current_turn = game.turn
	build_ui()
	$GameContents/Deck/GameDeck.card_selected.connect(select_card)
	$GameContents/MainArea/CenterContainer/GameBoard.board_clicked.connect(board_clicked)
	ingame_system.should_update_gui.connect(update_gui)
	ingame_system.restart_timer.connect(restart_timer)
	ingame_system.game_end.connect(end_game)
	ingame_system.check_connection_status()
	ingame_system.listen_to_host()

func _process(delta):
	ingame_system.check_connection_status()
	ingame_system.listen_to_host()
	update_time(delta)
	update_turn()

func update_turn():
	if current_turn != game.turn:
		current_turn = game.turn
		if current_turn:
			$GameContents/Turn.text = "YOUR TURN"
		else:
			$GameContents/Turn.text = "OPPONENT TURN"

func update_time(delta):
	delta_time += delta
	if floor(delta_time) > time_in_timer and not stop_timer:
		time_in_timer = floor(delta_time)
		if game.time_limit - time_in_timer >= 0:
			var seconds = (game.time_limit - time_in_timer)%60
			var time_string : String = "%02d" % seconds
			timer.text = time_string

func restart_timer():
	delta_time = 0
	time_in_timer = 0

func build_ui():
	var seconds = game.time_limit%60
	var time_string : String = "%02d" % seconds
	timer.text = time_string
	$GameContents/Header/EnemyInfo/Enemy.text = game.opponent.opponent_name
	if current_turn:
		$GameContents/Turn.text = "YOUR TURN"
	else:
		$GameContents/Turn.text = "OPPONENT TURN"
	$GameContents/Deck/GameDeck.deck = game.deck
	$GameContents/Deck/GameDeck.build_deck()
	$GameContents/MainArea/CenterContainer/GameBoard.board = game.board
	$GameContents/MainArea/CenterContainer/GameBoard.build_board()

func update_gui():
	build_ui()

func select_card(card_index : int):
	if selected_card_index != card_index:
		$GameContents/Deck/GameDeck.update_selected(card_index, selected_card_index)
		selected_card_index = card_index

func board_clicked(coords : Vector2i):
	if selected_card_index != -1:
		var card_data = $GameContents/Deck/GameDeck.get_card_data_from_index(selected_card_index)
		var igm = InGameCard.new()
		igm.exists = true
		igm.card_owner = game.number
		igm.current_dominator = game.number
		igm.card = card_data
		var move : GameMove = GameMove.new(game.number, selected_card_index, coords, igm)
		ingame_system.send_move(move)
		$GameContents/Deck/GameDeck.update_selected(-1, selected_card_index)
		selected_card_index = -1

func end_game(info : EndGameInfo):
	stop_timer = true
	$GameContents/Deck/GameDeck.update_selected(-1, selected_card_index)
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
