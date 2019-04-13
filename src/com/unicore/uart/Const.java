package com.unicore.uart;



 public final class Const {
	
	
	public static interface Error{
		
		public static final int SECURITY_EXCEPTION = 1;
		
		public static final int IO_EXCEPTION = 2;
		
		public static final int PARAMETER_IS_NULL = 3;
		
		public static final int NULLPOINT_EXCEPTION = 4;
	}
	
	public static interface Success{
		
		public static final int OPEN_SUCCESS  = 101;
		
		public static final int SUCCESS = 0;
		
		public static final int FAIL = -1;
		
	}
	
	public static interface Path{
		
		/**
		 * QR Trig
		 */
		public static final String QR_STRING = "/sys/devices/soc.0/slm753_dev.70/qr_trig";
		/**
		 * RFID and Psam2
		 */
		public static final String UART0 = "/dev/ttyHSL0";
		/**
		 * QR and Psam1
		 */
		public static final String UART1 = "/dev/ttyHSL1";
		/**
		 * Node higth(1) PSAM1   
		 * Node low(0)  QR 
		 */
		public static final String UART_SWT_1 = "sys/devices/soc.0/slm753_dev.70/uart1_swt";
		/**
		 * Node higth(1) RFID  
		 * Node low(0)   PSAM2
		 */
		public static final String UART_SWT_2 = "sys/devices/soc.0/slm753_dev.70/uart2_swt";
		/**
		 *psam1 Rest 
		 */
		public static final String PSAM_RST1 = "sys/devices/soc.0/slm753_dev.70/psam_rst";
		
		/**
		 *psam2 Rest 
		 */
		public static final String PSAM_RST2 = "sys/devices/soc.0/slm753_dev.70/psam2_rst";
		/**
		 * psam power enable
		 */
		public static final String PSAM_PWR_EN = "sys/devices/soc.0/slm753_dev.70/psam_pwr_en";
		
		/**
		 * RFID power enable
		 */
		public static final String RFID_PWR_EN = "sys/devices/soc.0/slm753_dev.70/rfid_pwr_en";
	}
	
	public static interface Cmd{
		/**
		 * psam power on
		 */
		public static final byte[] POWER_ON = {0x62,0x00,0x00,0x00,0x00,0x00,0x02,0x00,0x00,0x00,0x60};
		/**
		 * psam power off 
		 */
		public static final byte[] POWER_OFF = {0x63,0x00,0x00,0x00,0x00,0x00,0x04,0x00,0x00,0x00,0x67};
		/**
		 * psam execute apdu
		 */
		public static final byte[] EXE_APDU = {0x6F,0x05,0x00,0x00,0x00,0x00,0x03,0x00,0x00,0x00,0x00,(byte) 0x84,0x00,0x00,0x08,(byte) 0xE5};
	}

}
