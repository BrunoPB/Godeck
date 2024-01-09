extends Node

const menu_layout_path = "VerticalLayout/Menu/MenuLayout"
var main_menu_paths = {
		"shop_button" : menu_layout_path+"/ShopButton",
		"deck_builder_button" : menu_layout_path+"/DeckBuilderButton",
		"home_button" : menu_layout_path+"/HomeButton",
		"friends_button" : menu_layout_path+"/FriendsButton",
		"events_button" : menu_layout_path+"/EventsButton",
		"content_area" : "VerticalLayout/ContentArea"
	}

enum menu_screen_enum {
	HOME,
	DECKBUILDER,
	SHOP,
	FRIENDS,
	EVENTS
}
