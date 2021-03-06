package main

import "net/http"

// Handler for creating and destroying sessions.
func HandleIdentifyEndpoint(request APIRequest) APIResponse {
	var session APISession

	if request.Method == "NewSession" {
		session = createSession(request)
		return APIResponse{http.StatusOK, session.SessionKey}
	}

	if request.Method == "InvalidateSession" {
		invalidateSession(request.SessionKey)
		return ResponseOK
	}

	return ResponseInvalidMethod
}
