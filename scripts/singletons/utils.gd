extends Node

func remove_children(node):
	for child in node.get_children():
		node.remove_child(child)
		child.queue_free()

func create_hexagon(initial_x, initial_y, diameter):
	var hex = Polygon2D.new()
	var vetices = PackedVector2Array
	
	
