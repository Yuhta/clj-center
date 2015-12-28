#!/usr/bin/env ruby
require 'cljd/client'

with_cljd_client do |c|
  fail unless c.eval('(+ 1 1)').any? { |m| m['value'] == '2' }
end
