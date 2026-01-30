package net.sfelabs.knox_enterprise


    /**
     * Provide a valid Knox Platform for Enterprise (KPE) license as a constant. This technique is used for demonstration purposes
     * only.
     * Consider using more secure approach for passing your license key in a commercial scenario.
     * Visit https://docs.samsungknox.com/dev/common/knox-licenses.htm for details
     */
    object Constants {
        /**
         * NOTE:
         * There are three types of license keys available:
         * - KPE Development Key
         * - KPE Commercial Key - Standard
         * - KPE Commercial Key - Premium
         *
         * The KPE Development and KPE Commercial Key - Standard can be generated from KPP while a KPE
         * Commercial Key can be bought from a reseller.
         *
         * You can read more about how to get a licenses at:
         * https://docs.samsungknox.com/dev/common/tutorial-get-a-license.htm
         *
         * Do not hardcode license keys in an actual commercial scenario
         * TODO Implement a secure mechanism to pass KPE key to your application
         *
         */
        // TODO Enter the KPE Development, KPE Standard license key or KPE Premium license key
        //const val KPE_LICENSE_KEY = "KLM06-GOH5V-9WB6U-5QDNT-H8K6R-YGKC3"
        // License keys have been moved to the knox-licensing module

        //Hard coded cloud key
        //const val KPE_LICENSE_KEY = "KLM09-XXXXX-XXXXX-XXXXX-XXXXX-XXXXX"
        // TODO: Enter a backwards-compatible key
        /**
         * Find more information about how to use the backward-compatibility key here:
         * https://docs.samsungknox.com/dev/knox-sdk/faqs/licensing/what-backwards-compatible-key.htm
         *
         * The button to activate the backwards compatibility key will only appear if the
         * device is between Knox version 2.5 and 2.7.1 / Knox API 17 to 21
         *
         * For more details see:
         * https://docs.samsungknox.com/dev/knox-sdk/faqs/licensing/what-backwards-compatible-key.htm
         */
        const val BACKWARDS_COMPATIBLE_KEY = ""

    }

