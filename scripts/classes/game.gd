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
	var json = JSON.new()
	json.parse(board_string)
	var data =  json.data
	for col in data.size():
		for row in data[col].size():
			var item = data[col][row]
			if item == null:
				board[col][row] = null
			else:
				board[col][row] = InGameCard.new(item)

func set_deck(deck_string : String):
	deck = []
	var json = JSON.new()
	json.parse(deck_string)
	var data =  json.data
	for card in data:
		deck.append(InGameCard.new(card))

func set_opponent(opponent_string : String):
	var json = JSON.new()
	json.parse(opponent_string)
	var data =  json.data
	opponent.opponent_name = data.name
