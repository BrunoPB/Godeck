extends Node

class_name EndGameInfo

var winner : int
var reason : String
var gold : int
var ranking : int

func set_from_string(data : String):
	var json = JSON.new()
	json.parse(data)
	winner = json.data.winner
	reason = json.data.reason
	gold = json.data.gold
	ranking = json.data.ranking
