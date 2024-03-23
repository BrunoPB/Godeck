extends Node

class_name JSON_Utils

static func get_object_from_string(s : String):
	var json = JSON.new()
	json.parse(s)
	return json.data
