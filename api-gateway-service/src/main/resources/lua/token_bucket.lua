local key = KEYS[1];

local capacity = tonumber(ARGV[1])
local refill_rate = tonumber(ARGV[2])
local now = tonumber(ARGV[3])
local ttl = tonumber(ARGV[4])

local data = redis.call("HMGET", key, "tokens", "timestamp")
local tokens = tonumber(data[1])
local lastrefill = tonumber(data[2])

if tokens == nil then
    tokens = capacity
    lastrefill = now;
end

local elapsed = now - lastrefill
local refill = elapsed * refill_rate
tokens = math.min(capacity, tokens + refill)

local allowed = 0

if tokens > 0 then
    tokens = tokens - 1
    allowed = 1
end

local seconds_to_full = math.ceil((capacity - tokens) / refill_rate)
local reset_time = now + seconds_to_full

redis.call("HMSET", key, "tokens", tokens, "timestamp", now)

redis.call("EXPIRE", key, ttl)

return {allowed, tokens, reset_time}