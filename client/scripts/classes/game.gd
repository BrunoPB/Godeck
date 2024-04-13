extends Node

class_name Game

var board : Array
var deck : Array
var turn : bool
var number : int
var opponent : Opponent = Opponent.new()
var time_limit : int

func set_board(board_string : String):
	for col in 5:
		var r : Array = []
		for row in 5:
			r.append(null)
		board.append(r)
	var data = JSON_Utils.get_object_from_string(board_string)
	update_board(data)

func update_board(new_board : Array):
	for col in new_board.size():
		for row in new_board[col].size():
			var item = new_board[col][row]
			if item == null:
				board[col][row] = null
			else:
				board[col][row] = InGameCard.new(item)


func set_deck(deck_string : String):
	var data = JSON_Utils.get_object_from_string(deck_string)
	update_deck(data)

func update_deck(new_deck : Array):
	deck = []
	for card in new_deck:
		deck.append(InGameCard.new(card))

func set_opponent(opponent_string : String):
	var data = JSON_Utils.get_object_from_string(opponent_string)
	opponent.display_name = data.displayName

# TODO: Full validations
func validate_move(move : GameMove) -> bool:
	if not turn:
		return false
	if move.deck_index < 0 or move.deck_index >= deck.size():
		return false
	if not deck[move.deck_index].exists:
		return false
	if board[move.coords.x][move.coords.y] == null:
		return false
	if board[move.coords.x][move.coords.y].exists:
		return false
	return true

func predict_move(move : GameMove):
	var card = move.in_game_card
	var coords = move.coords
	board[coords.x][coords.y] = card
	deck[move.deck_index].exists = false
	turn = false
	execute_domination(move)

func execute_domination(move : GameMove):
	var coords = move.coords
	var column = coords.x
	var row = coords.y
	var card_data = move.in_game_card
	var compared_card
	var value

	# North
	if row > 0:
		compared_card = board[column][row - 1]
		if compared_card != null and compared_card.exists:
			value = card_data.card.north
			if value > compared_card.card.south:
				compared_card.current_dominator = move.player
	
	# North East
	if column % 2 == 0:
		if column < 4:
			compared_card = board[column + 1][row]
	else:
		if column < 4 and row > 0:
			compared_card = board[column + 1][row - 1]
	if compared_card != null and compared_card.exists:
		value = card_data.card.north_east
		if value > compared_card.card.south_west:
			compared_card.current_dominator = move.player
	
	# South East
	if column % 2 == 0:
		if column < 4 and row < 4:
			compared_card = board[column + 1][row + 1]
	else:
		if column < 4:
			compared_card = board[column + 1][row]
	if compared_card != null and compared_card.exists:
		value = card_data.card.south_east
		if value > compared_card.card.north_west:
			compared_card.current_dominator = move.player
	

	# South
	if row < 4:
		compared_card = board[column][row + 1]
		if compared_card != null and compared_card.exists:
			value = card_data.card.south
			if value > compared_card.card.north:
				compared_card.current_dominator = move.player

	# South West
	if column % 2 == 0:
		if column > 0 and row < 4:
			compared_card = board[column - 1][row + 1]
	else:
		if column > 0:
			compared_card = board[column - 1][row]
	if compared_card != null and compared_card.exists:
		value = card_data.card.south_west
		if value > compared_card.card.north_east:
			compared_card.current_dominator = move.player
	
	# North West
	if column % 2 == 0:
		if column > 0:
			compared_card = board[column - 1][row]
	else:
		if column > 0 and row > 0:
			compared_card = board[column - 1][row - 1]
	if compared_card != null and compared_card.exists:
		value = card_data.card.north_west
		if value > compared_card.card.south_east:
			compared_card.current_dominator = move.player
