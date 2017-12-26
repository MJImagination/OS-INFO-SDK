package util.SevenZip;

public final class InfoType {
  public final static int TPYE_PROCESSOR_ID = 1;
  public final static int TYPE_BASEBOARD_SERIAL = TPYE_PROCESSOR_ID + 1;
  public final static int TYPE_HD_SERIAL = TYPE_BASEBOARD_SERIAL + 1;
  public final static int TYPE_MAC_ADDRESS = TYPE_HD_SERIAL + 1;
  public final static int TYPE_IP_ADDRESS = TYPE_MAC_ADDRESS + 1;
  public final static int TYPE_BIOSS_SERIAL = TYPE_IP_ADDRESS + 1;
  public final static int TYPE_GATEWAY_IP = TYPE_BIOSS_SERIAL + 1;
  public final static int TYPE_GATEWAY_MAC = TYPE_GATEWAY_IP + 1;
  public final static int TYPE_PROCESSOR_ARCHITECTURE = TYPE_GATEWAY_MAC + 1;
  public final static int TYPE_OS_VERSION = TYPE_PROCESSOR_ARCHITECTURE + 1;
  public final static int TYPE_PUBLIC_IP = TYPE_OS_VERSION + 1;
  public final static int TYPE_IMEI = TYPE_PUBLIC_IP + 1;
  public final static int TYPE_LOCATION = TYPE_IMEI + 1;
  public final static int TYPE_CLIENT_PUSH_TIME = TYPE_LOCATION + 1;
  public final static int TYPE_COMPUTER_NAME = TYPE_CLIENT_PUSH_TIME + 1;
  public final static int TYPE_CPU_NAME = TYPE_COMPUTER_NAME + 1;
}

