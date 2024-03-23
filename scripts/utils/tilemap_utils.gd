extends Node

class_name Tilemap_Utils

static func build_empty_in_coords(tilemap : TileMap, coords : Variant):
	for i in coords:
		tilemap.set_cell(0, coords[i], 0, Vector2i(0,0), 1)
	tilemap.update_internals()

static func get_tile(tilemap : TileMap, coord : Vector2i):
	var coordsArray : Array = tilemap.get_used_cells(0)
	var index : int = coordsArray.find(coord)
	return tilemap.get_child(index)
