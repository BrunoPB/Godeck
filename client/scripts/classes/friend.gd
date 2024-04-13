extends Node

class_name Friend

var id : String
var username : String
var display_name : String
var deck : Array
var collection : Array

func _init(_id, _username, _display_name, _deck, _collection):
	id = _id
	username = _username
	display_name = _display_name
	deck = _deck
	collection = _collection
