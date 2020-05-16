package com.hand13;

public enum SpecialValue {
    VOID{
        @Override
        public String toString() {
            return "#<void>";
        }
    }
}
