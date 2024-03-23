extends Node

class_name Dictionary_Utils

static func dictionary_has_item(dictionary : Dictionary, item) -> bool:
	for key in dictionary:
		if dictionary[key] == item:
			return true
	return false
