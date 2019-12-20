// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: VulcanTransferProto.proto

package org.huangyr.project.vulcan.proto;

/**
 * <pre>
 * 心跳结果命令
 * </pre>
 *
 * Protobuf enum {@code org.huangyr.project.vulcan.proto.Command}
 */
public enum Command
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <pre>
   * 心跳包 更新心跳和租约信息
   * </pre>
   *
   * <code>NORMAL = 0;</code>
   */
  NORMAL(0),
  /**
   * <pre>
   * 关闭Runner
   * </pre>
   *
   * <code>SHUTDOWN = 1;</code>
   */
  SHUTDOWN(1),
  /**
   * <pre>
   * 更新Runner
   * </pre>
   *
   * <code>UPGRADE = 2;</code>
   */
  UPGRADE(2),
  UNRECOGNIZED(-1),
  ;

  /**
   * <pre>
   * 心跳包 更新心跳和租约信息
   * </pre>
   *
   * <code>NORMAL = 0;</code>
   */
  public static final int NORMAL_VALUE = 0;
  /**
   * <pre>
   * 关闭Runner
   * </pre>
   *
   * <code>SHUTDOWN = 1;</code>
   */
  public static final int SHUTDOWN_VALUE = 1;
  /**
   * <pre>
   * 更新Runner
   * </pre>
   *
   * <code>UPGRADE = 2;</code>
   */
  public static final int UPGRADE_VALUE = 2;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static Command valueOf(int value) {
    return forNumber(value);
  }

  public static Command forNumber(int value) {
    switch (value) {
      case 0: return NORMAL;
      case 1: return SHUTDOWN;
      case 2: return UPGRADE;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<Command>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      Command> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<Command>() {
          public Command findValueByNumber(int number) {
            return Command.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return org.huangyr.project.vulcan.proto.VulcanTransferProto.getDescriptor().getEnumTypes().get(0);
  }

  private static final Command[] VALUES = values();

  public static Command valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private Command(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:org.huangyr.project.vulcan.proto.Command)
}

