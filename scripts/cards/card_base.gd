extends Node2D

'''
Another possible approach I thought of was to instance the
card with the correct data rather than instancing a default
one then setting the stats to what they should be. However, 
due to how instancing new cells in tilemaps work, the curr
ent approach seems to be more intuitive.

My current guess on the general approach to instancing already
prepared cells would be to create new packed scene tiles and then
instance those. Downsides I can see are the necessity of keeping
track with new ids and the need to create said scene tiles in 
the first place. Upsides are the lack of editing needed post 
instancing alongside await no longer being necessary (guess).
'''


# Allows for editing card data post instancing
func set_card(card_data):
	# Since the textures simply do not exist as of yet, we can't load them.
	#$Layout/CardHex.texture = load(card_data.file_name)
	
	# We, however, already have card stats, so we can use those.
	$Layout/Stats/North.text = str(card_data.north)
	$Layout/Stats/NorthSide/NorthWest.text = str(card_data.north_west)
	$Layout/Stats/NorthSide/NorthEast.text = str(card_data.north_east)
	$Layout/Stats/SouthSide/SouthWest.text = str(card_data.south_west)
	$Layout/Stats/SouthSide/SouthEast.text = str(card_data.south_east)
	$Layout/Stats/South.text = str(card_data.south)
