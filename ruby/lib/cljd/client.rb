require 'bencode'
require 'json'
require 'logger'
require 'socket'
require 'uri'

module NREPL
  LOGGER = Logger.new(STDOUT)
  LOGGER.level = Logger::WARN

  class Client
    def self.open(uri)
      c = new(uri)
      yield c
    ensure
      c.close if c
    end

    def initialize(uri)
      uri = URI(uri) unless uri.is_a?(URI)
      @io = case uri.scheme
            when 'nrepl' then TCPSocket.new(uri.host, uri.port)
            when 'nreplds' then UNIXSocket.new(uri.path)
            else raise(ArgumentError, "unsupported schema `#{uri.scheme}'")
            end
      @io.autoclose = true
    end

    def sync_send(msg)
      @io.print(msg.bencode)
      LOGGER.debug(">>> #{msg.inspect}")
      parser = BEncode::Parser.new(@io)
      responses = []
      begin
        r = parser.parse!
        LOGGER.debug("<<< #{r.inspect}")
        responses << r
      end until r['status'] && r['status'].include?('done')
      responses
    end

    def eval(code, ns = nil)
      msg = {op: 'eval', code: code}
      unless ns.nil?
        eval("(require (symbol #{ns.to_json}))")
        msg[:ns] = ns
      end
      sync_send(msg)
    end

    def close
      @io.close
    end
  end
end

def with_cljd_client(&block)
  file = File.join(ENV['HOME'], '.cljd-nrepl-port')
  port = IO.read(file).to_i
  NREPL::Client.open("nrepl://localhost:#{port}", &block)
end
