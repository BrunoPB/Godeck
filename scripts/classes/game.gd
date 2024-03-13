extends Node

class_name Game

var board : Array
var deck : Array
var turn : bool
var number : int
var opponent : Opponent = Opponent.new()
var time_limit : int

func set_board(board_string : String):
	pass

func set_deck(deck_string : String):
	pass

func set_opponent(opponent_string : String):
	var json = JSON.new()
	json.parse(opponent_string)
	opponent.opponent_name = json.data.name
