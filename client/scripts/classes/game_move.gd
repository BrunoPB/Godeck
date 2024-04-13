extends Node

class_name GameMove

var player : int
var deck_index : int
var coords : Vector2i
var in_game_card : InGameCard

func _init(_player : int, _deck_index : int, _coords : Vector2i, _in_game_card : InGameCard):
	player = _player
	deck_index = _deck_index
	coords = _coords
	in_game_card = _in_game_card

func toJSONString() -> String:
	return "{\"player\":" + str(player) + ",\"deckIndex\":" + str(deck_index) + ",\"coords\":" + "{\"x\":" + str(coords.x) + ",\"y\":" + str(coords.y) + "}" + ",\"inGameCard\":" + in_game_card.toJSONString() + "}"
