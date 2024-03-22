extends Node

class_name EndGameInfo

@onready var json_utils = get_node("/root/JsonUtils")

var winner : int
var reason : String
var gold : int
var ranking : int

func set_from_string(data : String):
	var json = JSON.new()
	json.parse(data)
	var info =  json.data
	winner = info.winner
	reason = info.reason
	gold = info.gold
	ranking = info.ranking
