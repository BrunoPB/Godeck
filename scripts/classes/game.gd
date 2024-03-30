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
		var r : Array
		for row in 5:
			r.append(null)
		board.append(r)
	var data = JSON_Utils.get_object_from_string(board_string)
	for col in data.size():
		for row in data[col].size():
			var item = data[col][row]
			if item == null:
				board[col][row] = null
			else:
				board[col][row] = InGameCard.new(item)

func set_deck(deck_string : String):
	deck = []
	var data = JSON_Utils.get_object_from_string(deck_string)
	for card in data:
		deck.append(InGameCard.new(card))

func set_opponent(opponent_string : String):
	var data = JSON_Utils.get_object_from_string(opponent_string)
	opponent.display_name = data.displayName
