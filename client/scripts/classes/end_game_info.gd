extends Node

class_name EndGameInfo

var winner : int
var reason : String
var gold : int
var ranking : int

func set_from_string(data : String):
	var info =  JSON_Utils.get_object_from_string(data)
	winner = info.winner
	reason = info.reason
	gold = info.gold
	ranking = info.ranking
