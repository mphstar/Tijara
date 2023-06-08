package com.tijara;

import androidx.annotation.NonNull;

public enum CustomDialog {
    LOADING {
        @NonNull
        @Override
        public String toString() {
            return String.valueOf(R.raw.loading);
        }

    },
    CONFIRMATION {
        @NonNull
        @Override
        public String toString() {
            return String.valueOf(R.raw.confirmation);
        }

    },
    SUCCESS {
        @NonNull
        @Override
        public String toString() {
            return String.valueOf(R.raw.success);
        }
    },
    ERROR {
        @NonNull
        @Override
        public String toString() {
            return String.valueOf(R.raw.error);
        }
    },
    WARNING {
        @NonNull
        @Override
        public String toString() {
            return String.valueOf(R.raw.warning);
        }
    },
    ;

}
