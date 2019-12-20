// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: VulcanTransferProto.proto

package org.huangyr.project.vulcan.proto;

/**
 * <pre>
 * 心跳结果报
 * </pre>
 *
 * Protobuf type {@code org.huangyr.project.vulcan.proto.ServerCommandPackage}
 */
public  final class ServerCommandPackage extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:org.huangyr.project.vulcan.proto.ServerCommandPackage)
    ServerCommandPackageOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ServerCommandPackage.newBuilder() to construct.
  private ServerCommandPackage(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ServerCommandPackage() {
    resultCode_ = 0;
    command_ = 0;
    message_ = "";
    responseHeartTime_ = 0L;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ServerCommandPackage(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 8: {

            resultCode_ = input.readInt32();
            break;
          }
          case 16: {
            int rawValue = input.readEnum();

            command_ = rawValue;
            break;
          }
          case 26: {
            java.lang.String s = input.readStringRequireUtf8();

            message_ = s;
            break;
          }
          case 32: {

            responseHeartTime_ = input.readUInt64();
            break;
          }
          default: {
            if (!parseUnknownFieldProto3(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.huangyr.project.vulcan.proto.VulcanTransferProto.internal_static_org_huangyr_project_vulcan_proto_ServerCommandPackage_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.huangyr.project.vulcan.proto.VulcanTransferProto.internal_static_org_huangyr_project_vulcan_proto_ServerCommandPackage_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.huangyr.project.vulcan.proto.ServerCommandPackage.class, org.huangyr.project.vulcan.proto.ServerCommandPackage.Builder.class);
  }

  public static final int RESULTCODE_FIELD_NUMBER = 1;
  private int resultCode_;
  /**
   * <pre>
   * 响应结果 / 消息标识
   * </pre>
   *
   * <code>int32 resultCode = 1;</code>
   */
  public int getResultCode() {
    return resultCode_;
  }

  public static final int COMMAND_FIELD_NUMBER = 2;
  private int command_;
  /**
   * <pre>
   * Server指令
   * </pre>
   *
   * <code>.org.huangyr.project.vulcan.proto.Command command = 2;</code>
   */
  public int getCommandValue() {
    return command_;
  }
  /**
   * <pre>
   * Server指令
   * </pre>
   *
   * <code>.org.huangyr.project.vulcan.proto.Command command = 2;</code>
   */
  public org.huangyr.project.vulcan.proto.Command getCommand() {
    @SuppressWarnings("deprecation")
    org.huangyr.project.vulcan.proto.Command result = org.huangyr.project.vulcan.proto.Command.valueOf(command_);
    return result == null ? org.huangyr.project.vulcan.proto.Command.UNRECOGNIZED : result;
  }

  public static final int MESSAGE_FIELD_NUMBER = 3;
  private volatile java.lang.Object message_;
  /**
   * <pre>
   * Server指令详情
   * </pre>
   *
   * <code>string message = 3;</code>
   */
  public java.lang.String getMessage() {
    java.lang.Object ref = message_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      message_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * Server指令详情
   * </pre>
   *
   * <code>string message = 3;</code>
   */
  public com.google.protobuf.ByteString
      getMessageBytes() {
    java.lang.Object ref = message_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      message_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int RESPONSEHEARTTIME_FIELD_NUMBER = 4;
  private long responseHeartTime_;
  /**
   * <pre>
   * 消息发送时间
   * </pre>
   *
   * <code>uint64 responseHeartTime = 4;</code>
   */
  public long getResponseHeartTime() {
    return responseHeartTime_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (resultCode_ != 0) {
      output.writeInt32(1, resultCode_);
    }
    if (command_ != org.huangyr.project.vulcan.proto.Command.NORMAL.getNumber()) {
      output.writeEnum(2, command_);
    }
    if (!getMessageBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, message_);
    }
    if (responseHeartTime_ != 0L) {
      output.writeUInt64(4, responseHeartTime_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (resultCode_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, resultCode_);
    }
    if (command_ != org.huangyr.project.vulcan.proto.Command.NORMAL.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(2, command_);
    }
    if (!getMessageBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, message_);
    }
    if (responseHeartTime_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt64Size(4, responseHeartTime_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof org.huangyr.project.vulcan.proto.ServerCommandPackage)) {
      return super.equals(obj);
    }
    org.huangyr.project.vulcan.proto.ServerCommandPackage other = (org.huangyr.project.vulcan.proto.ServerCommandPackage) obj;

    boolean result = true;
    result = result && (getResultCode()
        == other.getResultCode());
    result = result && command_ == other.command_;
    result = result && getMessage()
        .equals(other.getMessage());
    result = result && (getResponseHeartTime()
        == other.getResponseHeartTime());
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + RESULTCODE_FIELD_NUMBER;
    hash = (53 * hash) + getResultCode();
    hash = (37 * hash) + COMMAND_FIELD_NUMBER;
    hash = (53 * hash) + command_;
    hash = (37 * hash) + MESSAGE_FIELD_NUMBER;
    hash = (53 * hash) + getMessage().hashCode();
    hash = (37 * hash) + RESPONSEHEARTTIME_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getResponseHeartTime());
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.huangyr.project.vulcan.proto.ServerCommandPackage parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.huangyr.project.vulcan.proto.ServerCommandPackage parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.huangyr.project.vulcan.proto.ServerCommandPackage parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.huangyr.project.vulcan.proto.ServerCommandPackage parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.huangyr.project.vulcan.proto.ServerCommandPackage parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.huangyr.project.vulcan.proto.ServerCommandPackage parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.huangyr.project.vulcan.proto.ServerCommandPackage parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.huangyr.project.vulcan.proto.ServerCommandPackage parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.huangyr.project.vulcan.proto.ServerCommandPackage parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.huangyr.project.vulcan.proto.ServerCommandPackage parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.huangyr.project.vulcan.proto.ServerCommandPackage parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.huangyr.project.vulcan.proto.ServerCommandPackage parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(org.huangyr.project.vulcan.proto.ServerCommandPackage prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   * 心跳结果报
   * </pre>
   *
   * Protobuf type {@code org.huangyr.project.vulcan.proto.ServerCommandPackage}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:org.huangyr.project.vulcan.proto.ServerCommandPackage)
      org.huangyr.project.vulcan.proto.ServerCommandPackageOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.huangyr.project.vulcan.proto.VulcanTransferProto.internal_static_org_huangyr_project_vulcan_proto_ServerCommandPackage_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.huangyr.project.vulcan.proto.VulcanTransferProto.internal_static_org_huangyr_project_vulcan_proto_ServerCommandPackage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.huangyr.project.vulcan.proto.ServerCommandPackage.class, org.huangyr.project.vulcan.proto.ServerCommandPackage.Builder.class);
    }

    // Construct using org.huangyr.project.vulcan.proto.ServerCommandPackage.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      resultCode_ = 0;

      command_ = 0;

      message_ = "";

      responseHeartTime_ = 0L;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.huangyr.project.vulcan.proto.VulcanTransferProto.internal_static_org_huangyr_project_vulcan_proto_ServerCommandPackage_descriptor;
    }

    @java.lang.Override
    public org.huangyr.project.vulcan.proto.ServerCommandPackage getDefaultInstanceForType() {
      return org.huangyr.project.vulcan.proto.ServerCommandPackage.getDefaultInstance();
    }

    @java.lang.Override
    public org.huangyr.project.vulcan.proto.ServerCommandPackage build() {
      org.huangyr.project.vulcan.proto.ServerCommandPackage result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.huangyr.project.vulcan.proto.ServerCommandPackage buildPartial() {
      org.huangyr.project.vulcan.proto.ServerCommandPackage result = new org.huangyr.project.vulcan.proto.ServerCommandPackage(this);
      result.resultCode_ = resultCode_;
      result.command_ = command_;
      result.message_ = message_;
      result.responseHeartTime_ = responseHeartTime_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return (Builder) super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof org.huangyr.project.vulcan.proto.ServerCommandPackage) {
        return mergeFrom((org.huangyr.project.vulcan.proto.ServerCommandPackage)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.huangyr.project.vulcan.proto.ServerCommandPackage other) {
      if (other == org.huangyr.project.vulcan.proto.ServerCommandPackage.getDefaultInstance()) return this;
      if (other.getResultCode() != 0) {
        setResultCode(other.getResultCode());
      }
      if (other.command_ != 0) {
        setCommandValue(other.getCommandValue());
      }
      if (!other.getMessage().isEmpty()) {
        message_ = other.message_;
        onChanged();
      }
      if (other.getResponseHeartTime() != 0L) {
        setResponseHeartTime(other.getResponseHeartTime());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      org.huangyr.project.vulcan.proto.ServerCommandPackage parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.huangyr.project.vulcan.proto.ServerCommandPackage) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private int resultCode_ ;
    /**
     * <pre>
     * 响应结果 / 消息标识
     * </pre>
     *
     * <code>int32 resultCode = 1;</code>
     */
    public int getResultCode() {
      return resultCode_;
    }
    /**
     * <pre>
     * 响应结果 / 消息标识
     * </pre>
     *
     * <code>int32 resultCode = 1;</code>
     */
    public Builder setResultCode(int value) {
      
      resultCode_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 响应结果 / 消息标识
     * </pre>
     *
     * <code>int32 resultCode = 1;</code>
     */
    public Builder clearResultCode() {
      
      resultCode_ = 0;
      onChanged();
      return this;
    }

    private int command_ = 0;
    /**
     * <pre>
     * Server指令
     * </pre>
     *
     * <code>.org.huangyr.project.vulcan.proto.Command command = 2;</code>
     */
    public int getCommandValue() {
      return command_;
    }
    /**
     * <pre>
     * Server指令
     * </pre>
     *
     * <code>.org.huangyr.project.vulcan.proto.Command command = 2;</code>
     */
    public Builder setCommandValue(int value) {
      command_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * Server指令
     * </pre>
     *
     * <code>.org.huangyr.project.vulcan.proto.Command command = 2;</code>
     */
    public org.huangyr.project.vulcan.proto.Command getCommand() {
      @SuppressWarnings("deprecation")
      org.huangyr.project.vulcan.proto.Command result = org.huangyr.project.vulcan.proto.Command.valueOf(command_);
      return result == null ? org.huangyr.project.vulcan.proto.Command.UNRECOGNIZED : result;
    }
    /**
     * <pre>
     * Server指令
     * </pre>
     *
     * <code>.org.huangyr.project.vulcan.proto.Command command = 2;</code>
     */
    public Builder setCommand(org.huangyr.project.vulcan.proto.Command value) {
      if (value == null) {
        throw new NullPointerException();
      }
      
      command_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * Server指令
     * </pre>
     *
     * <code>.org.huangyr.project.vulcan.proto.Command command = 2;</code>
     */
    public Builder clearCommand() {
      
      command_ = 0;
      onChanged();
      return this;
    }

    private java.lang.Object message_ = "";
    /**
     * <pre>
     * Server指令详情
     * </pre>
     *
     * <code>string message = 3;</code>
     */
    public java.lang.String getMessage() {
      java.lang.Object ref = message_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        message_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     * Server指令详情
     * </pre>
     *
     * <code>string message = 3;</code>
     */
    public com.google.protobuf.ByteString
        getMessageBytes() {
      java.lang.Object ref = message_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        message_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * Server指令详情
     * </pre>
     *
     * <code>string message = 3;</code>
     */
    public Builder setMessage(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      message_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * Server指令详情
     * </pre>
     *
     * <code>string message = 3;</code>
     */
    public Builder clearMessage() {
      
      message_ = getDefaultInstance().getMessage();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * Server指令详情
     * </pre>
     *
     * <code>string message = 3;</code>
     */
    public Builder setMessageBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      message_ = value;
      onChanged();
      return this;
    }

    private long responseHeartTime_ ;
    /**
     * <pre>
     * 消息发送时间
     * </pre>
     *
     * <code>uint64 responseHeartTime = 4;</code>
     */
    public long getResponseHeartTime() {
      return responseHeartTime_;
    }
    /**
     * <pre>
     * 消息发送时间
     * </pre>
     *
     * <code>uint64 responseHeartTime = 4;</code>
     */
    public Builder setResponseHeartTime(long value) {
      
      responseHeartTime_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 消息发送时间
     * </pre>
     *
     * <code>uint64 responseHeartTime = 4;</code>
     */
    public Builder clearResponseHeartTime() {
      
      responseHeartTime_ = 0L;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:org.huangyr.project.vulcan.proto.ServerCommandPackage)
  }

  // @@protoc_insertion_point(class_scope:org.huangyr.project.vulcan.proto.ServerCommandPackage)
  private static final org.huangyr.project.vulcan.proto.ServerCommandPackage DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.huangyr.project.vulcan.proto.ServerCommandPackage();
  }

  public static org.huangyr.project.vulcan.proto.ServerCommandPackage getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ServerCommandPackage>
      PARSER = new com.google.protobuf.AbstractParser<ServerCommandPackage>() {
    @java.lang.Override
    public ServerCommandPackage parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new ServerCommandPackage(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ServerCommandPackage> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ServerCommandPackage> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.huangyr.project.vulcan.proto.ServerCommandPackage getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

