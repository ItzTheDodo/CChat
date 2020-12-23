package me.ItzTheDodo.CChat.ChatRooms.utils;

@SuppressWarnings("unused")
public enum RankLevel {
	
	NEW {
		@Override
		public String toString() {
			return "NEW";
		}
		
		public int getLevel() {
			return 0;
		}
	},
	MEMBER {
		@Override
		public String toString() {
			return "MEMBER";
		}
		
		public int getLevel() {
			return 1;
		}
	},
	TRUSTED {
		@Override
		public String toString() {
			return "TRUSTED";
		}
		
		public int getLevel() {
			return 2;
		}
	},
	STAFF {
		@Override
		public String toString() {
			return "STAFF";
		}
		
		public int getLevel() {
			return 3;
		}
	}, 
	CO_OWNER {
		@Override
		public String toString() {
			return "CO_OWNER";
		}
		
		public int getLevel() {
			return 4;
		}
	},
	OWNER {
		@Override
		public String toString() {
			return "OWNER";
		}
		
		public int getLevel() {
			return 5;
		}
	}

}
