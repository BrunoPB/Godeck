extends Node

class_name Node_Utils

static func remove_children(node):
	for child in node.get_children():
		node.remove_child(child)
		child.queue_free()
